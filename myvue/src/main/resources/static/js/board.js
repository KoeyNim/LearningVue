$(document).ready(function() {
    var app = new Vue({
        el: '#app',
        data: {
            message: '안녕하세요 Vue!',
        },
    })
    const regist = new Vue({
        el: '#regist',
        data: {
            board: {
                title: "test1",
                content: "test2",
            }
        },
        methods: {
            save() {
                const ex = !!this.id;
                const board = Object.assign({}, this.board);
                console.log(ex);
                console.log(board);
                $.ajax({
                    type: ex ? 'PUT' : 'POST',
                    url: '/board' + (ex ? '/modify'+ '/' + this.id : '/create'),
                    contentType: 'application/json; charset=UTF-8',
                    data: JSON.stringify(board),
                    beforeSend(xhr) {
                       var header = $("meta[name='_csrf_header']").attr("content");
                       var token = $("meta[name='_csrf']").attr("content");
                       xhr.setRequestHeader(header, token);
                    }
                }).done(function() {
                    alert('글이 등록되었습니다.');
                    location.href = '/board';
                }).fail(() => alert('잘못된 요청입니다.'));
            },
            cancel() {
                location.href = '/board';
            }
        }
    })
});