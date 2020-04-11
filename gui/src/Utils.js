import tinydate from 'tinydate';

export const getQueryParamByName = (name, url) => {
    if (!url) url = window.location.href;
    name = name.replace(/[[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}

export const formatDate = (date) => {
    const stamp = tinydate('{DD} {MMMM} {YYYY}', {
        MMMM: d => date.toLocaleString('default', { month: 'long' }),
        DD: d => date.getDate()
    });

    return stamp(date);
}

export const formatTime = (date) => {
    const stamp = tinydate('{HH}:{mm} {A}', {
        A: date => date.getHours() >= 12 ? 'PM' : 'AM',
        HH: date => {
            const h = date.getHours();
            return h > 12 ? h - 12 : h;
        }
    });

    return stamp(date);
}
