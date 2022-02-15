$(document).ready(function() {

    const regist = new Vue({
        el: '#regist',
        data: {
            board: {
            }
        },
        created() {
            const me = this;
            // insert or update
            me.id = URLSearch.get('id');
            if (!me.id) return;
            $.ajax(API_VERSION + '/board/update/' + me.id, {
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
            save() {
                const ex = !!this.board.id;
                const board = Object.assign({}, this.board);
                $.ajax({
                    type: ex ? 'PUT' : 'POST',
                    url: API_VERSION + '/board' + (ex ? '/update' + '/' + this.id : '/create'),
                    contentType: 'application/json; charset=UTF-8',
                    data: JSON.stringify(board),
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
            cancel() {
                location.href = '/board';
            }
        }
    })
});