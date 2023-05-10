let vu;
$(() => {
    // 새로고침시 히스토리 초기화
    if(performance.getEntriesByType("navigation")[0].type === 'reload') {
        history.replaceState(null, '', location.pathname);
    }

    vu = new Vue({
        el: '#content',
        data: {
            srch: !!history.state ? history.state.srch : {
            	page:0,
            	size:5,
            	sort:'boardSeqno,desc',
            	srchKey: undefined,
            	srchVal: undefined,
            },
            page: !!history.state ? history.state.page : {
            	pageCount: 5,
            	totalPages: 0,
            	numbers:[],
            },
            list:{},
            options: pageSizeArr,
            srchOptions: [
                {text: '전체', value: undefined},
                {text: '제목', value: 'title'},
                {text: '작성자', value: 'userid'},
            ],
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
                    url: API_VERSION + '/board',
                    data: me.srch
                }).done((res) => {
                    console.log('done', res, arguments);
                    Object.assign(me.page, res);
                    me.list = res.content;
                    // 페이지 번호 계산
                    me.page.numbers = setPagination(me.page.totalPages , me.page.number+1, me.page.pageCount);
                    // 히스토리에 검색 데이터 저장
                    history.replaceState({srch: me.srch, page: me.page}, '', location.pathname);
                }).fail(() => alert('잘못된 요청입니다.')); // TODO fail
            },
            fnPage(e, page) {
                console.log('fnPage', arguments);
                let me = this;

                // 요청페이지가 존재하지 않거나 존재하지만 최소값 보다 낮을 경우 0으로 초기화
                if (!page || (!!page && page < 0)) {
                    me.srch.page = 0;
                    me.fnGets(e);
                    return;
                }

                // 요청 페이지 번호가 total값 보다 높을 경우 total값으로 초기화
            	if (me.page.totalPages > 0 && page >= me.page.totalPages) {
            	    me.srch.page = me.page.totalPages -1;
            	    me.fnGets(e);
            	    return;
            	}
            	me.srch.page = page;
            	me.fnGets(e);
            },
            fnSrch(e) {
            	console.log('fnSrch', arguments);
            	let me = this;
            	Object.assign(me.srch, {page:0});
            	me.fnGets(e);
            },
            fnRegist(e) {
            	console.log('fnRegist', arguments);
            	location.href ='/board-form';
            },
            fnDetail(e, boardSeqno) {
            	console.log('fnDetail', arguments);
            	sessionStorage.setItem('boardSeqno', boardSeqno);
            	location.href ='/board-detail';
            },
            fnDwldExcel(e) {
            	console.log('fnDwldExcel', arguments);
            	location.href= API_VERSION + '/board/excel';
            },
        }
    });
});