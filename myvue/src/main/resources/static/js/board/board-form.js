let vu;
$(document).ready(function() {
    vu = new Vue({
        el: '#page',
        data: {
            result:{},
            imgFile:[]
        },
        created() {
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
                console.log(data);
            }).fail(() => alert('잘못된 요청입니다.'));
        },
        methods: {
            $fileSelect : function $fileSelect(){ 
                this.imgFile = this.$refs.imgFile.files[0];
                console.log(this.imgFile);
            },
            fnSave() {
                const ex = !!this.result.id;
                const formData = new FormData();
                formData.append('title', this.result.title);
                formData.append('content', this.result.content);
                formData.append('imgFile', this.imgFile);
                $.ajax({
                    type: ex ? 'PUT' : 'POST',
                    url: API_VERSION + '/board' + (ex ? '/update/' + this.result.id : '/create'),
                    enctype: 'multipart/form-data',
                    processData: false,
                    contentType: false,
                    cache: false,
                    data: formData,
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
            },
            fnCancel() {
                location.href = '/board';
            }
        }
    })
});