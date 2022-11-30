let vu;
$(() => {
    const boardSeqno = sessionStorage.getItem('boardSeqno');

    vu = new Vue({
        el: '#page',
        data: {
            result: {},
            filePath: undefined,
        },
        created() {
            let me = this;
            me.fnGets();
        },
        methods: {
            fnGets(e) {
                console.log('fnGets', arguments);
                let me = this;
                $ajax.api({
                    url: API_VERSION + '/board/detail',
                    data: {boardSeqno, boardSeqno},
                    async: false
                }).done(res => {
                    console.log('done', res, arguments);
                    me.result = res;
                    if(!!me.result?.fileEntity) me.filePath = API_VERSION + '/file/download/' + me.result.fileEntity.fileSeqno;
                });
            },
            fnUpdate(e, boardSeqno) {
                console.log('fnUpdate', arguments);
                location.href = '/board-form?boardSeqno=' + boardSeqno;
            },
            fnDelete(e) {
                console.log('fnDelete', arguments);
                if(!confirm('이 게시글을 삭제하시겠습니까?')) return;
                $ajax.api({
                    url: API_VERSION + '/board/delete',
                    type: 'DELETE',
                    data: {boardSeqno, boardSeqno}
                }).done((res) => {
                    console.log('done', res, arguments);
                    alert(res.message);
                    location.href = '/board';
                });
            }
        }
    })
});