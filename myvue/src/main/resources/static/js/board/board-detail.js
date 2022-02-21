let vu;
$(document).ready(function() {
    vu = new Vue({
        el: '#page',
        data: {
            result: {}
        },
        created() {
            this.fnLoad();
        },
        methods: {
            fnLoad() {
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
                    me.result = response;
                    console.log(response);
                }).fail(() => alert('잘못된 요청입니다.'));
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