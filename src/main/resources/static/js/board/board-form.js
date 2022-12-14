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
                imgNmList: []
            },
            delImgList: []
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
                    /* 에디터 이미지 업로드 기능 사용시 호출 **/
                    onImageUpload : (images) => {
                        me.fnUploadImage(images[0]);
                    },
                    /* 에디터 내부 content 변경시 호출 **/
                    onChange : (contents) => {
                        me.result.content = contents;
                    },
                    /* 에디터 이미지 삭제버튼 클릭시 호출 **/
                    onMediaDelete : (img) => {
                        let imgNm = img[0].attributes.getNamedItem('img-name').value;
                        if(!!boardSeqno) {
                            me.delImgList.push(imgNm);
                        } else {
                            me.result.imgNmList = me.result.imgNmList.filter((el) => {return el !== imgNm;});
                        }
                    }
                }
            });
//            /* 에디터 내부 이미지 파일명 리스트 **/
//            if(!!$($('img[name=innerImg]')[0]).attr('img-name')) {
//                $('img[name=innerImg]').each((index, item) => {
//                    me.result.imgNmList.push(item.attributes.getNamedItem('img-name').value);
//                })
//            }
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
                    Object.assign(me.result, res);
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
                        if(!!(Array.isArray(me.result.imgNmList) && me.result.imgNmList.length !== 0)) {
                            formData.append('imgNmList', me.result.imgNmList);
                        }

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
                            /* 게시글 수정시 DB 삭제 **/
                            if(!!(!!boardSeqno && Array.isArray(me.delImgList) && me.delImgList.length !== 0)) {
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
            /* summernote 이미지 업로드 **/
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
                    me.result.imgNmList.push(res);
                    /* img tag 생성 **/
                    $('#summernote').summernote('insertImage', API_VERSION + '/image/find/'+ res, ($img) => {
                        $img.attr('name', 'innerImg');
                        $img.attr('img-name', res);
                        $img.css('width', "50%");
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