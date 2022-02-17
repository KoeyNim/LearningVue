$(document).ready(function() {
    const vu = new Vue({
        el: '#page',
        data: {
            board: {}
        },
        created() {
            const me = this;
            me.id = URLSearch.get('id');
            $.ajax(API_VERSION + '/board/find/' + me.id, {
                async: false,
                beforeSend(xhr) {
                    var header = $("meta[name='_csrf_header']").attr("content");
                    var token = $("meta[name='_csrf']").attr("content");
                    xhr.setRequestHeader(header, token);
                }
            }).done(data => {
                me.board = data;
            }).fail(() => alert('잘못된 요청입니다.'));
        },
        methods: {
            fnUpdate() {
                location.href = '/board/regist?id=' + this.id;
            },
            fnDelete() {
                if(!confirm('이 게시글을 삭제하시겠습니까?')) return;
                $.ajax({
                    type: 'DELETE',
                    url: API_VERSION + '/board/delete/' + this.id,
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