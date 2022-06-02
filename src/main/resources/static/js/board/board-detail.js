let vu;
$(document).ready(function() {
    vu = new Vue({
        el: '#page',
        data: {
            result: {},
            filePath:'',
        },
        created() {
            this.fnLoad();
        },
        methods: {
            fnLoad() {
                const me = this;
                me.result.id = URLSearch.get('id');
                ajaxAPI('GET', API_VERSION + '/board/find/' + me.result.id, undefined, {async: false}
                ).done(response => {
                    me.result = response;
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
                ajaxAPI('DELETE', API_VERSION + '/board/delete/' + this.result.id
                ).done((response) => {
                    alert(response.message);
                    location.href = '/board';
                }).fail((response) => {
                    alert('게시글 삭제 오류');
                    console.log(response.responseJSON.message);
                });
            },
            fnList() {
                location.href = '/board';
            }
        }
    })
});