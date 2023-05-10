let vu;
$(() => {
  const boardSeqno = URLSearch.get('boardSeqno');;

  vu = new Vue({
    el: '#content',
    mixins: [textEditorMixin],
    data: {},
    created() {
      if(!!boardSeqno) {
        let me = this;
        me.fnGets();
      }
    },
    mounted() {
    },
    methods: {
      fnGets(e) {
          console.log('fnGets', arguments);
          let me = this;
          $ajax.api({
              url: API_VERSION + '/board/detail',
              data: {boardSeqno : boardSeqno},
              async: false
          }).done((res) => {
              console.log('done', arguments);
              Object.assign(me.result, res);
          });
      },
      fnSave(e) {
          console.log('fnSave', arguments);
          let me = this;
          let isBoardSeqno = !!boardSeqno;
          me.$validator.validateAll().then((success) => {
              if(success) {
                  if (me.isEditor == 'S') {
                      if ($('#summernote').summernote('isEmpty')) {
                          alert('내용을 입력해주세요.');
                          return;
                      }
                  }

                  const formData = new FormData();
                  formData.append('title', me.result.title);
                  formData.append('content', me.result.content);
                  if(me.$refs.file.files[0] instanceof File) formData.append('file', me.$refs.file.files[0]);
                  if(isBoardSeqno) formData.append('boardSeqno', boardSeqno);
                  if(!!(Array.isArray(me.result.imgList) && me.result.imgList.length !== 0)) {
                      formData.append('imgListJson', JSON.stringify(me.result.imgList));
                  }

                  $ajax.api({
                      url: API_VERSION + '/board' + (isBoardSeqno ? '/update' : '/create'),
                      type: isBoardSeqno ? 'PUT' : 'POST',
                      data: formData,
                      enctype: 'multipart/form-data',
                      processData: false,
                      contentType: false,
                      cache: false
                  }).done((res) => {
                      console.log('done', arguments);
                      alert(res.message);
                      /** 게시글 수정시 DB 삭제 */
                      if(!!(isBoardSeqno && Array.isArray(me.delImgList) && me.delImgList.length !== 0)) {
                          $ajax.api({
                              url: API_VERSION + '/image/delete',
                              type: 'DELETE',
                              data: {delImgList: me.delImgList},
                              async: false,
                          }).done((res) => {
                              console.log('done', arguments);
                              location.href = '/board';
                          });
                      } else {
                          location.href = '/board';
                      }
                  });
              }
          });
      },
    }
  })
});