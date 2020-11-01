import tinydate from 'tinydate';

export const getValidationRegex = (dataType) => {
    // Note that HTML5 engines wraps the whole pattern inside ^(?: and )$ constructs
    switch (dataType) {
        case 'PATH':
            return '(((.{0,2}/)(.?[a-zA-Z0-9_\\-\\*])*)+)';
        default:
            return '';
    }
}

export const getQueryParamByName = (name, url) => {
    if (!url) url = window.location.href;
    name = name.replace(/[[\]]/g, '\\$&');
    const regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)');
    const results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}

export const getArrayQueryParamByName = (name, url) => {
    if (!url) url = decodeURIComponent(window.location.search);
    name = name.replace(/[[\]]/g, "\\$&");
    const regex = new RegExp(`[?|&](${name})\\[(\\d+)\\]\\.(\\w+)=(\\w+)`, 'gm');

    let match, result = [];
    while ((match = regex.exec(url)) !== null) {
        result[match[2]] = result[match[2]] || {};
        result[match[2]][match[3]] = match[4];
    }
    return result;
}

export const getQueryParamsFromFilter = (filter) => {
    const queryParamStr = `?pageable.pageNumber=${filter.pageable.pageNumber}`
        + `&pageable.pageSize=${filter.pageable.pageSize}`
        + `&pageable.sort.sortBy=${filter.pageable.sort.sortBy}`
        + `&pageable.sort.sortOrder=${filter.pageable.sort.sortOrder}&`
        + filter.conditions.reduce((a, c, i) => a +
            `conditions%5B${i}%5D.key=${c.key}&conditions%5B${i}%5D.operator=${c.operator}&conditions%5B${i}%5D.value=${c.value}`, '');

    return queryParamStr;
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

export function deepCompare() {
    var i, l, leftChain, rightChain;

    function compare2Objects(x, y) {
        var p;

        // remember that NaN === NaN returns false
        // and isNaN(undefined) returns true
        if (isNaN(x) && isNaN(y) && typeof x === 'number' && typeof y === 'number') {
            return true;
        }

        // Compare primitives and functions.
        // Check if both arguments link to the same object.
        // Especially useful on the step where we compare prototypes
        if (x === y) {
            return true;
        }

        // Works in case when functions are created in constructor.
        // Comparing dates is a common scenario. Another built-ins?
        // We can even handle functions passed across iframes
        if ((typeof x === 'function' && typeof y === 'function') ||
            (x instanceof Date && y instanceof Date) ||
            (x instanceof RegExp && y instanceof RegExp) ||
            (x instanceof String && y instanceof String) ||
            (x instanceof Number && y instanceof Number)) {
            return x.toString() === y.toString();
        }

        // At last checking prototypes as good as we can
        if (!(x instanceof Object && y instanceof Object)) {
            return false;
        }

        if (x.isPrototypeOf(y) || y.isPrototypeOf(x)) {
            return false;
        }

        if (x.constructor !== y.constructor) {
            return false;
        }

        if (x.prototype !== y.prototype) {
            return false;
        }

        // Check for infinitive linking loops
        if (leftChain.indexOf(x) > -1 || rightChain.indexOf(y) > -1) {
            return false;
        }

        // Quick checking of one object being a subset of another.
        // todo: cache the structure of arguments[0] for performance
        for (p in y) {
            if (y.hasOwnProperty(p) !== x.hasOwnProperty(p)) {
                return false;
            }
            else if (typeof y[p] !== typeof x[p]) {
                return false;
            }
        }

        for (p in x) {
            if (y.hasOwnProperty(p) !== x.hasOwnProperty(p)) {
                return false;
            }
            else if (typeof y[p] !== typeof x[p]) {
                return false;
            }

            switch (typeof (x[p])) {
                case 'object':
                case 'function':

                    leftChain.push(x);
                    rightChain.push(y);

                    if (!compare2Objects(x[p], y[p])) {
                        return false;
                    }

                    leftChain.pop();
                    rightChain.pop();
                    break;

                default:
                    if (x[p] !== y[p]) {
                        return false;
                    }
                    break;
            }
        }

        return true;
    }

    if (arguments.length < 1) {
        return true; //Die silently? Don't know how to handle such case, please help...
        // throw "Need two or more arguments to compare";
    }

    for (i = 1, l = arguments.length; i < l; i++) {

        leftChain = []; //Todo: this can be cached
        rightChain = [];

        if (!compare2Objects(arguments[0], arguments[i])) {
            return false;
        }
    }

    return true;
}
