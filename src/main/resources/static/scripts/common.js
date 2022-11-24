/* ajax API **/
const $ajax = {
    api: (options) => {
        /* GET Type은 csrf 미적용 **/
        if (!!options.type) {
            let csrf = {
                beforeSend(xhr) {
                    var header = $("meta[name='_csrf_header']").attr("content");
                    var token = $("meta[name='_csrf']").attr("content");
                    xhr.setRequestHeader(header, token);
            }};
            Object.assign(options, csrf);
        };
        return $.ajax(Object.assign({
            statusCode: {
                200: (...arguments) => {
                    console.log('200', arguments);
                },
                401: (...arguments) => {
                    console.log('401', arguments);
                    alert('인증되지 않음. 401');
                },
                404: (...arguments) => {
                    console.log('404', arguments);
                    alert('서비스를 찾을 수 없음. 404');
                },
                500: (...arguments) => {
                    console.log('500', arguments);
                    alert('서버 오류 발생. 500');
                },
                503: (...arguments) => {
                    console.log('503', arguments);
                    alert('서버 오류 발생. 503');
                }
            }
        }, options
        )).done((...arguments) => {
            console.log('done prepare', arguments);
        }).fail((...arguments) => {
            console.log('fail prepare', arguments);
        })
    }
};

/* 현재 페이지 블록의 페이지 번호 리스트 **/
function setPagination(totalPages, currentPage, size) {
    let currentPagingBlockNumber = Math.ceil(currentPage/size);
    let blockPageNumber = (currentPagingBlockNumber * size);
    let startPageNumber = blockPageNumber - (size -1);
    let endPageNumber = blockPageNumber > totalPages ? totalPages : blockPageNumber;
    let page = [];
    
    for (let i=startPageNumber; i<=endPageNumber; i++) {
        page.push(i);
    }
    return page;
}

/* ajax api 사용하지 않음.
function ajaxAPI(type, url, data, options) {

    let ajaxParam = {
        type: type,
        url: url,
    };

    if (data) {
        Object.assign(ajaxParam, {data: data});
    };

    if (options) {
        Object.assign(ajaxParam, options);
    };

    console.log("type : ", type, ", ", "url : ", url, ", ", "data : ", data);

    // GET 방식에서는 csrf를 적용할 필요가 없음.
    if (type !== 'GET') {
        let csrf = {
            beforeSend(xhr) {
                var header = $("meta[name='_csrf_header']").attr("content");
                var token = $("meta[name='_csrf']").attr("content");
                xhr.setRequestHeader(header, token);
            }};
        Object.assign(ajaxParam, csrf);
    };
    return $.ajax(ajaxParam);
}
**/