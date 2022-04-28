let vu;
$(document).ready(function() {
    vu = new Vue({
        el: '#page',
        data: {
            result: {},
            filePath:'',
            authUserId:{},
        },
        created() {
            this.fnLoad();
        },
        methods: {
            fnLoad() {
                console.log(location.origin);
                const me = this;
                me.result.id = URLSearch.get('id');
                $.ajax(API_VERSION + '/board/find/' + me.result.id, {
                    async: false,
                    beforeSend(xhr) {
                        var header = $("meta[name='_csrf_header']").attr("content");
                        var token = $("meta[name='_csrf']").attr("content");
                        xhr.setRequestHeader(header, token);
                    }
                }).done(response => {
                    me.result = response.data;
                    me.authUserId = response.authUserId;
                    me.filePath = me.result.fileEntity ? API_VERSION + '/download/' + me.result.fileEntity.id : '';
                    console.log(response);
                }).fail(() => {
                    alert('잘못된 요청입니다.');
                    location.href = '/board';
                });
            },
            fnUpdate() {
                location.href = '/board/regist?id=' + this.result.id;
            },
            fnDelete() {
                if(!confirm('이 게시글을 삭제하시겠습니까?')) return;
                $.ajax({
                    type: 'DELETE',
                    url: API_VERSION + '/board/delete/' + this.result.id,
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
            fnList() {
                location.href = '/board';
            }
        }
    })
});