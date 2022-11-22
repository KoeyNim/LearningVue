let vu;
$().ready(() => {
    const boardSeqno = URLSearch.get('boardSeqno');;

    vu = new Vue({
        el: '#page',
/*        components: {
            // Use the <ckeditor> component in this view.
            ckeditor: CKEditor.component
        },*/
        data: {
            result:{},
            imgFile:[],
            
            // CKEditor
/*            editor: ClassicEditor,
            editorConfig: {
                extraPlugins: [customUploadAdapter], // 이미지 업로드 어댑터
                mediaEmbed: {previewsInData: true}, // media oembed 태그안에 iframe 태그를 생성
                language: 'ko',
            }*/
        },
        created() {
            if(!!boardSeqno) {
                let me = this;
                me.fnGets();
            }
        },
        mounted() {
            let me = this;
            $('#summernote').summernote({
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
                    onImageUpload : function(images) {
                        uploadImage(images[0]);
                    },
                    onChange : function(contents) {
                        me.result.content = contents
                    }
                }
            });
        },
        methods: {
            // ck-editor toolbar 변경
/*            onReady(editor)  {
                editor.ui.getEditableElement().parentElement.insertBefore(
                    editor.ui.view.toolbar.element,
                    editor.ui.getEditableElement()
                );
            },*/
            fnGets(e) {
                console.log('fnGets', arguments);
                let me = this;
                ajaxAPI('GET', API_VERSION + '/board/detail', {boardSeqno : boardSeqno}, {async: false}
                ).done((response) => {
                    me.result = response;
                });
            },
            fnSave(e) {
                console.log('fnSave', arguments);
                let me = this;
                me.$validator.validateAll().then(success => {
                    if(success){
                        if ($('#summernote').summernote('isEmpty')) {
                            alert('내용을 입력해주세요.');
                            return;
                        }
                        if(me.imgFile instanceof File) {
                            const formData = new FormData();
                            const options = {
                                enctype: 'multipart/form-data',
                                processData: false,
                                contentType: false,
                                cache: false,
                                async: false,
                            };
                            formData.append('imgFile', me.imgFile);
                            ajaxAPI('POST', API_VERSION + '/fileupload', formData, options
                            ).done((res) => {
                                console.log('done', arguments);
                                Object.assign(me.result, {fileEntity : res});
                            });
                        }
                        ajaxAPI(!!boardSeqno ? 'PUT' : 'POST', 
                                API_VERSION + '/board' + (!!boardSeqno ? '/update' : '/create'), 
                                JSON.stringify(me.result),
                                {contentType: 'application/json; charset=UTF-8', cache: false}
                        ).done((res) => {
                            console.log('done', arguments);
                            alert(res.message);
                            location.href = '/board';
                        });
                    }
                });
            },
            fnFileSelect(e) {
                console.log('fnFileSelect', arguments);
                let me = this
                me.imgFile = me.$refs.imgFile.files[0];
                console.log(me.imgFile);
            }
        }
    })
    // summernote image upload
    function uploadImage(image) {
        const formData = new FormData();
        const options = {
            enctype : 'multipart/form-data',
            contentType : false,
            processData : false,
            cache : false,
        };
        formData.append('image', image);
        ajaxAPI('POST', API_VERSION + '/imageupload', formData, options
        ).done((res) => {
            console.log('done', arguments);
            $('#summernote').summernote('insertImage', url, function($image) {
                $image.css('width', "50%");
            });
        });
    }
    
    // CKEditor image upload
/*    function customUploadAdapter(editor) {
        editor.plugins.get('FileRepository').createUploadAdapter = (loader) => {
            return new UploadAdapter(loader, editor)
        }
    }*/
});