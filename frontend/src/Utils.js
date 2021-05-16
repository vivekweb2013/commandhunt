import tinydate from "tinydate";

export const getValidationRegex = (dataType) => {
    // Note that HTML5 engines wraps the whole pattern inside ^(?: and )$ constructs
    switch (dataType) {
        case "PATH":
            return "((\\.{0,2}/(?!/))(\\.?[a-zA-Z0-9_*-])*)+|\"((\\.{0,2}/(?!/))(\\.?[ ]*[a-zA-Z0-9_*-])*)+\"";
        case "NUMBER":
            return "[0-9]*";
        case "PERMISSION":
            return "[0-9]{3}";
        default:
            return "[\\s\\S]*"; // match anything
    }
};

export const getQueryParamByName = (name, url) => {
    if (!url) { url = window.location.href; }
    name = name.replace(/[[\]]/g, "\\$&");
    const regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)");
    const results = regex.exec(url);
    if (!results) { return null; }
    if (!results[2]) { return ""; }
    return decodeURIComponent(results[2].replace(/\+/g, " "));
};

export const getArrayQueryParamByName = (name, url) => {
    if (!url) { url = decodeURIComponent(window.location.search); }
    name = name.replace(/[[\]]/g, "\\$&");
    const regex = new RegExp(`[?|&](${name})\\[(\\d+)\\]\\.(\\w+)=(\\w+)`, "gm");

    let match, result = [];
    while ((match = regex.exec(url)) !== null) {
        result[match[2]] = result[match[2]] || {};
        result[match[2]][match[3]] = match[4];
    }
    return result;
};

export const getQueryParamsFromFilter = (filter) => {
    const queryParamStr = `?pagination.pageNumber=${filter.pagination.pageNumber}`
        + `&pagination.pageSize=${filter.pagination.pageSize}`
        + `&pagination.sort.by=${filter.pagination.sort.by}`
        + `&pagination.sort.order=${filter.pagination.sort.order}&`
        + filter.conditions.reduce((a, c, i) => a +
            `conditions%5B${i}%5D.key=${c.key}&conditions%5B${i}%5D.operator=${c.operator}&conditions%5B${i}%5D.value=${c.value}`, "");

    return queryParamStr;
};

export const formatDate = (date) => {
    const stamp = tinydate("{DD} {MMMM} {YYYY}", {
        MMMM: (d) => date.toLocaleString("default", { month: "long" }),
        DD: (d) => date.getDate()
    });

    return stamp(date);
};

export const formatTime = (date) => {
    const stamp = tinydate("{HH}:{mm} {A}", {
        A: (d1) => (d1.getHours() >= 12) ? "PM" : "AM",
        HH: (d2) => {
            const h = d2.getHours();
            return (h > 12) ? h - 12 : h;
        }
    });

    return stamp(date);
};

export const arrayEquals = (array1, array2) => {
    const array2Sorted = array2.slice().sort();
    return array1.length === array2.length && array1.slice().sort().every(function (value, index) {
        return value === array2Sorted[index];
    });
};
