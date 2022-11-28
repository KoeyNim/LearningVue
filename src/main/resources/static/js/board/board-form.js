let vu;
$(() => {
    const boardSeqno = URLSearch.get('boardSeqno');;

    vu = new Vue({
        el: '#page',
/*        components: {
            // Use the <ckeditor> component in this view.
            ckeditor: CKEditor.component
        },*/
        data: {
            result:{
                editorImage: undefined
            },
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
                    onImageUpload : (images) => {
                        me.fnUploadImage(images[0]);
                    },
                    onChange : (contents) => {
                        me.result.content = contents;
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
                $ajax.api({
                    url: API_VERSION + '/board/detail',
                    data: {boardSeqno : boardSeqno},
                    async: false
                }).done((res) => {
                    console.log('done', arguments);
                    me.result = res;
                });
            },
            fnSave(e) {
                console.log('fnSave', arguments);
                let me = this;
                me.$validator.validateAll().then((success) => {
                    if(success) {
                        if ($('#summernote').summernote('isEmpty')) {
                            alert('내용을 입력해주세요.');
                            return;
                        }

                        const formData = new FormData();
                        formData.append('title', me.result.title);
                        formData.append('content', me.result.content);
                        if(me.$refs.file.files[0] instanceof File) formData.append('file', me.$refs.file.files[0]);
                        if(!!boardSeqno) formData.append('boardSeqno', boardSeqno);
                        if(!!me.result.editorImage) formData.append('image', JSON.stringify(me.result.editorImage));

                        $ajax.api({
                            url: API_VERSION + '/board' + (!!boardSeqno ? '/update' : '/create'),
                            type: !!boardSeqno ? 'PUT' : 'POST',
                            data: formData,
                            enctype: 'multipart/form-data',
                            processData: false,
                            contentType: false,
                            cache: false
                        }).done((res) => {
                            console.log('done', arguments);
                            alert(res.message);
                            location.href = '/board';
                        });
                    }
                });
            },
            /* summernote 이미지 업로드 **/
            fnUploadImage(image) {
                console.log('fnUploadImage', arguments);
                let me = this;

                const formData = new FormData();
                formData.append('image', image);
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
                    me.result.editorImage = {};
                    Object.assign(me.result.editorImage, res);
                    $('#summernote').summernote('insertImage', API_VERSION + '/image/find/'+ res.fileNm , ($image) => {
                        $image.css('width', "50%");
                    });
                });
            }
        }
    })

    // CKEditor image upload
/*    function customUploadAdapter(editor) {
        editor.plugins.get('FileRepository').createUploadAdapter = (loader) => {
            return new UploadAdapter(loader, editor)
        }
    }*/
});