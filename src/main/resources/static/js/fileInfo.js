window.onload = function () {
    let mediaElements = document.querySelectorAll('.post-files img, .post-files video');
    mediaElements.forEach(function (media) {
        let fileInfo = media.previousElementSibling;
        let fileNameSpan = fileInfo.querySelector('.file-name');
        let fileSizeDimensionsSpan = fileInfo.querySelector('.file-size-dimensions');

        let mediaElement = media.tagName === 'IMG' ? new Image() : document.createElement('video');
        mediaElement.src = media.src;

        mediaElement.onload = mediaElement.onloadedmetadata = function () {
            let width = mediaElement.videoWidth || mediaElement.width;
            let height = mediaElement.videoHeight || mediaElement.height;
            fileSizeDimensionsSpan.textContent += width + 'x' + height;
        };
    });
};
