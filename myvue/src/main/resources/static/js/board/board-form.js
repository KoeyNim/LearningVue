let vu;
$().ready(() => {
    vu = new Vue({
        el: '#page',
        data: {
            result:{},
            imgFile:[]
        },
        created() {
            this.fnLoad();
        },
        mounted() {
            const me = this;
            $('#summernote').summernote({
                height: 300,                  // 에디터 높이
                minHeight: null,              // 최소 높이
                maxHeight: null,              // 최대 높이
                focus: true,                  // 에디터 로딩후 포커스를 맞출지 여부
                lang: "ko-KR",                // 한글 설정
                placeholder: '최대 2048자까지 쓸 수 있습니다',    //placeholder 설정
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
                onChange : function(contents) {
                    me.result.content = contents
                }
            }
            });
            var markupStr = $('#summernote').summernote('code');
            console.log(markupStr);
            
        },
        methods: {
            $fileSelect : function $fileSelect(){ 
                this.imgFile = this.$refs.imgFile.files[0];
                console.log(this.imgFile);
            },
            fnLoad() {
                const me = this; 
                // insert or update
                me.result.id = URLSearch.get('id');
                if (!me.result.id) return;
                $.ajax(API_VERSION + '/board/find/' + me.result.id, {
                    async: false,
                    beforeSend(xhr) {
                        var header = $("meta[name='_csrf_header']").attr("content");
                        var token = $("meta[name='_csrf']").attr("content");
                        xhr.setRequestHeader(header, token);
                    }
                }).done(response => {
                    me.result = response.data;
                }).fail(() => {
                    alert('잘못된 요청입니다.');
                    location.href = '/board';
                });
            },
            fnSave() {
                this.$validator.validateAll().then(success => {
                    if(success){
                        if ($('#summernote').summernote('isEmpty')) {
                            alert('내용을 입력해주세요.');
                            return;
                        }
                        const ex = !!this.result.id;
                        let result = Object.assign({}, this.result);
                        let fileData = '';
                        if(this.imgFile instanceof File) {
                            const formData = new FormData();
                            formData.append('imgFile', this.imgFile);
                            $.ajax({
                                type: 'POST',
                                url: API_VERSION + '/fileupload',
                                enctype: 'multipart/form-data',
                                processData: false,
                                contentType: false,
                                cache: false,
                                async: false,
                                data: formData,
                                beforeSend(xhr) {
                                    var header = $("meta[name='_csrf_header']").attr("content");
                                    var token = $("meta[name='_csrf']").attr("content");
                                    xhr.setRequestHeader(header, token);
                                }
                            }).done(function(response) {
                                console.log(response);
                                fileData = response;
                            }).fail((response) => {
                                alert('xx', response.responseJSON);
                                console.log(arguments);
                                console.log(response);
                            });
                        }
                        if(fileData) {
                            result = Object.assign(this.result, {fileEntity : fileData});
                        }
                        console.log(result);
                        $.ajax({
                            type: ex ? 'PUT' : 'POST',
                            url: API_VERSION + '/board' + (ex ? '/update/' + this.result.id : '/create'),
                            contentType: 'application/json; charset=UTF-8',
                            cache: false,
                            data: JSON.stringify(result),
                            beforeSend(xhr) {
                                var header = $("meta[name='_csrf_header']").attr("content");
                                var token = $("meta[name='_csrf']").attr("content");
                                xhr.setRequestHeader(header, token);
                            }
                        }).done(function(response) {
                            alert(response.message);
                            console.log(response);
                            location.href = '/board';
                        }).fail((response) => {
                            alert('xx', response.responseJSON);
                            console.log(arguments);
                            console.log(response);
                        });
                    }
                });
            },
            fnCancel() {
                location.href = '/board';
            }
        }
    })
});