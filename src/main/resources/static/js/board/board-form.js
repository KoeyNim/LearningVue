let vu;
$(() => {
  const boardSeqno = URLSearch.get('boardSeqno');;

  vu = new Vue({
    el: '#content',
    mixins: [textEditorMixin],
    data: {},
    created() {
      let me = this;
      if(!!boardSeqno) me.fnGets();
    },
    mounted() {},
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
                  if (me.isEditor == 'S' && $('#summernote').summernote('isEmpty')) {
                      alert('내용을 입력해주세요.');
                      return;
                  }

                  const formData = new FormData();
                  formData.append('title', me.result.title);
                  formData.append('content', me.result.content);

                  if(isBoardSeqno) formData.append('boardSeqno', boardSeqno);
                  if(me.$refs.file.files[0] instanceof File) formData.append('file', me.$refs.file.files[0]);
                  if(!!(Array.isArray(me.result.imgList) && me.result.imgList.length !== 0)) formData.append('imgListJson', JSON.stringify(me.result.imgList));

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

                      /** 키값, 삭제할 이미지 리스트 체크 */
                      if (isBoardSeqno || !Array.isArray(me.delImgList) || me.delImgList.length == 0) {
                        location.href = '/board';
                        return;
                      }

                      /** 수정시 기존 이미지 데이터 삭제 */
                      $ajax.api({
                          url: API_VERSION + '/image/delete',
                          type: 'DELETE',
                          data: {delImgList: me.delImgList},
                          async: false,
                      }).done((res) => {
                          console.log('done', arguments);
                          location.href = '/board';
                      });
                  });
              }
          });
      },
    }
  })
});