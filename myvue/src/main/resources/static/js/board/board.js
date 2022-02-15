$(document).ready(function() {
    const getList = new Vue({
        el: '#getList',
        data: {
            list: [],
        },
        created() {
          this.getList();
        },
        methods: {
            regOrMod(id = '') {
                location.href ='/board/regist' + (id ? '?id=' + id : '');
            },
            getList() {
                const me = this;
                $.ajax({
                    type: 'GET',
                    url: API_VERSION + '/board',
                    beforeSend(xhr) {
                       var header = $("meta[name='_csrf_header']").attr("content");
                       var token = $("meta[name='_csrf']").attr("content");
                       xhr.setRequestHeader(header, token);
                    }
                }).done(function(response) {
                    me.list = response;
                }).fail(() => alert('잘못된 요청입니다.') );
            },
        }
    });
});
$(document).ajaxError((event, response) => {
    console.log('common error', event, response);
    alert(response.responseJSON.message);
});