let vu;
$(document).ready(function() {
    vu = new Vue({
        el: '#page',
        data: {
            result:{},
            imgFile:[]
        },
        created() {
            this.fnLoad();
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
                }).done(data => {
                    me.result = data;
                }).fail(() => {
                    alert('잘못된 요청입니다.');
                    location.href = '/board';
                });
            },
            fnSave() {
                this.$validator.validateAll().then(success => {
                    if(success){
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