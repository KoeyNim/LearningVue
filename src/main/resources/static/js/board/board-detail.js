let vu;
$(document).ready(function() {
    
    const boardSeqno = sessionStorage.getItem('boardSeqno');
    
    vu = new Vue({
        el: '#page',
        data: {
            result: {},
            filePath:'',
        },
        created() {
            let me = this;
            me.fnGets();
        },
        methods: {
            fnGets(e) {
                console.log('fnGets', arguments);
                let me = this;
                ajaxAPI('GET', API_VERSION + '/board/detail', {boardSeqno : boardSeqno}, {async: false}
                ).done(res => {
                    console.log('done', res, arguments);
                    me.result = res;
                    me.filePath = me.result.fileEntity ? API_VERSION + '/download/' + me.result.fileEntity.id : '';
                });
            },
            fnUpdate(e, boardSeqno) {
                console.log('fnUpdate', arguments);
                location.href = '/board-form?boardSeqno=' + boardSeqno;
            },
            fnDelete() {
                console.log('fnDelete', arguments);
                if(!confirm('이 게시글을 삭제하시겠습니까?')) return;
                ajaxAPI('DELETE', API_VERSION + '/board/delete', {boardSeqno : boardSeqno}
                ).done((res) => {
                    console.log('done', res, arguments);
                    alert(response.message);
                    location.href = '/board';
                });
            }
        }
    })
});