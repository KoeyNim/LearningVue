class UploadAdapter {
    constructor(loader, editor) {
        this.loader = loader;
        this.editor = editor;
    }
    upload() {
        const me = this;
        return this.loader.file.then(file => new Promise(((resolve, reject) => {
        const formData = new FormData();
        const options = {
            enctype : 'multipart/form-data',
            contentType : false,
            processData : false,
            cache : false,
        }
        formData.append('image', file);
        ajaxAPI("POST", API_VERSION + '/imageupload', formData, options
        ).done(function(url) {
            console.log(url);
            me.editor.execute( 'insertImage', { source: url } );
        }).fail((jqXHR, stat, err) => {
            console.log(stat+':'+err);
        });
        })))
    }
}
