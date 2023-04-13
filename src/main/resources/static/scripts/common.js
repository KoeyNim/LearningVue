/** ajax API */
const $ajax = {
    api: (options) => {
        /* GET Type은 csrf 미적용 **/
        if (!!options.type) {
            let csrf = {
                beforeSend(xhr) {
                    var header = $("meta[name='_csrf_header']").attr("content");
                    var token = $("meta[name='_csrf']").attr("content");
                    xhr.setRequestHeader(header, token);
            }};
            Object.assign(options, csrf);
        };
        return $.ajax(Object.assign({
            statusCode: {
                200: (...arguments) => {
                    console.log('200', arguments);
                },
                401: (...arguments) => {
                    console.log('401', arguments);
                    alert('인증되지 않음. 401');
                    if (xhr.responseJSON.errType === 'XMLHttpRequest') {
                        alert('세션 만료');
                        location.href = xhr.responseJSON.message;
                    }
                },
                404: (...arguments) => {
                    console.log('404', arguments);
                    alert('서비스를 찾을 수 없음. 404');
                },
                500: (...arguments) => {
                    console.log('500', arguments);
                    alert('서버 오류 발생. 500');
                },
                503: (...arguments) => {
                    console.log('503', arguments);
                    alert('서버 오류 발생. 503');
                }
            }
        }, options
        )).done((...arguments) => {
            console.log('done prepare', arguments);
        }).fail((...arguments) => {
            console.log('fail prepare', arguments);
        })
    }
};

/** 현재 페이지 블록의 페이지 번호 리스트 */
function setPagination(totalPages, currentPage, size) {
    let currentPagingBlockNumber = Math.ceil(currentPage/size);
    let blockPageNumber = (currentPagingBlockNumber * size);
    let startPageNumber = blockPageNumber - (size -1);
    let endPageNumber = blockPageNumber > totalPages ? totalPages : blockPageNumber;
    let page = [];
    
    for (let i=startPageNumber; i<=endPageNumber; i++) {
        page.push(i);
    }
    return page;
}

/** Vue 텍스트 에디터 Mixin */
const textEditorMixin = !location.pathname.includes('form') ? undefined : {
    data() {
        return {
            // default Editor
            isEditor: 'S', // S = summernote, C = ck-editor
            // summernote data
            result:{
              content: undefined,
              imgList: []
            },
            delImgList: [],
            // ckeditor data
            editor: ClassicEditor,
            editorConfig: {
                extraPlugins: [customUploadAdapter], // 이미지 업로드 어댑터
                mediaEmbed: {previewsInData: true}, // media oembed 태그안에 iframe 태그를 생성
                language: 'ko',
            }
        }
    },
    components: {
      // ckeditor component 사용
      ckeditor: CKEditor.component
    },
    created() {
    },
    mounted() {
      let me = this;
      if(!!me.result.content) $('#summernote').html(me.result.content);
      $('#summernote').summernote(summernoteOptions(me));
      /** 에디터 내부 이미지 파일명 리스트 */
//      if(!!$($('img[name=innerImg]')[0]).attr('img-name')) {
//          $('img[name=innerImg]').each((index, item) => {
//              me.result.imgList.push(item.attributes.getNamedItem('img-name').value);
//          })
//      }
    },
    methods: {
      // editor button event
      fnEditor(e) {
          console.log('fnEditor', arguments);
          let me = this;
          if (!me.isEditor) return;
          if (me.isEditor == 'S') {
              $('#summernote').summernote('destroy');
              $('#summernote').hide();
              e.target.value = 'summernote';
              e.target.innerText = 'summernote';
              me.isEditor = 'C';
              return;
          }

          if (me.isEditor == 'C') {
              $('#summernote').show();
              $('#summernote').html(me.result.content);
              $('#summernote').summernote(summernoteOptions(me));
              e.target.value = 'ck-editor';
              e.target.innerText = 'ck-editor';
              me.isEditor = 'S';
              return;
          }
      },
      // ckeditor toolbar 변경
      onReady(editor) {
        editor.ui.getEditableElement().parentElement.insertBefore(
            editor.ui.view.toolbar.element,
            editor.ui.getEditableElement()
        );
      },
      // summernote 이미지 업로드
      fnUploadImage(img) {
          console.log('fnUploadImage', arguments);
          let me = this;
  
          const formData = new FormData();
          formData.append('img', img);
          $ajax.api({
              url: API_VERSION + '/image/temp',
              type: 'POST',
              data: formData,
              enctype : 'multipart/form-data',
              contentType : false,
              processData : false,
              cache : false
          }).done((res) => {
              console.log('done', arguments);
              me.result.imgList.push(res);
              // img tag 생성
              $('#summernote').summernote('insertImage', API_VERSION + '/image/find/'+ res.imgNm, ($img) => {
                  $img.attr('name', 'innerImg');
                  $img.attr('img-name', res.imgNm);
                  $img.css('width', "50%");
              });
          });
      }
    }
}

/** summernote 옵션 */
function summernoteOptions(me) {
    return {
        height: 600,                  // 에디터 높이
        minHeight: null,              // 최소 높이
        maxHeight: null,              // 최대 높이
        focus: true,                  // 에디터 로딩후 포커스를 맞출지 여부
        lang: "ko-KR",                // 한글 설정
        placeholder: '최대 2048자까지 작성할 수 있습니다',    //placeholder 설정
        toolbar: [
            ['fontname', ['fontname']],
            ['fontsize', ['fontsize']],
            ['style', ['bold', 'italic', 'underline','strikethrough', 'clear']],
            ['color', ['forecolor']],
            ['table', ['table']],
            ['para', ['ul', 'ol', 'paragraph']],
            ['height', ['height']],
            ['insert',['picture','link','video']],
        ],
        fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋움체','바탕체'],
        fontSizes: ['8','9','10','11','12','14','16','18','20','22','24','28','30','36','50','72'],
        callbacks : {
            /** 에디터 이미지 업로드 기능 사용시 호출 */
            onImageUpload : (images) => {
                for(const image of images) me.fnUploadImage(image);
            },
            /** 에디터 내부 content 변경시 호출 */
            onChange : (contents) => {
                me.result.content = contents;
            },
            /** 에디터 이미지 삭제버튼 클릭시 호출 */
            onMediaDelete : (selectImg) => {
                let selectImgNm = selectImg[0].attributes.getNamedItem('img-name').value;
                !!boardSeqno ? me.delImgList.push(selectImgNm) : me.result.imgList = me.result.imgList.filter((el) => {return el.imgNm !== selectImgNm;});
            }
        }
    }
}

/** ckeditor 이미지 업로드 */
function customUploadAdapter(editor) {
    editor.plugins.get('FileRepository').createUploadAdapter = (loader) => {
        return new UploadAdapter(loader, editor)
    }
}