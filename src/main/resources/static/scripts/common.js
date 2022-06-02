function ajaxAPI(type, url, data, options) {

    let ajaxObj = {
        type: type,
        url: url,
    };

    if (data) {
        Object.assign(ajaxObj, {data: data});
    };

    if (options) {
        Object.assign(ajaxObj, options);
    };

    if (type !== 'GET') {
        let csrf = {
            beforeSend(xhr) {
                var header = $("meta[name='_csrf_header']").attr("content");
                var token = $("meta[name='_csrf']").attr("content");
                xhr.setRequestHeader(header, token);
            }};
        Object.assign(ajaxObj, csrf);
    };

    return $.ajax(ajaxObj);
}

function setPagination(totalPage, currentPage, pageSize) {
    let currentPagingBlockNumber = Math.ceil(currentPage/pageSize);
    let blockPageNumber = (currentPagingBlockNumber * pageSize);
    let startPageNumber = blockPageNumber - (pageSize -1);
    let endPageNumber = blockPageNumber > totalPage ? totalPage : blockPageNumber;
    let pagingList = [];
    
    for (let i=startPageNumber; i<=endPageNumber; i++) {
        pagingList.push(i);
    }
    return pagingList;
}