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