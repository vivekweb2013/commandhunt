import React, { Component } from 'react';
import './Pagination.scss';

class Pagination extends Component {

    render() {
        const { totalSize, totalPages, pageNumber, maxPagesToShow } = this.props;

        const visiblePages = totalPages > maxPagesToShow ? maxPagesToShow : totalPages;
        const visiblePageOffset = Math.trunc(visiblePages / 2);
        const startOfVisiblePages = (pageNumber + visiblePageOffset) <= totalPages ?
            ((pageNumber - visiblePageOffset) > 0 ? (pageNumber - visiblePageOffset) : 1) :
            totalPages - visiblePages + 1;

        return totalSize > 0 ? (
            <div className="pagination">
                <button type="button" onClick={() => this.props.handlePageChange(1)}>&laquo;</button>

                {Array.from(Array(visiblePages), (e, i) =>
                    <button type="button" key={startOfVisiblePages + i} disabled={pageNumber === (startOfVisiblePages + i)}
                        onClick={() => this.props.handlePageChange((startOfVisiblePages + i))}>
                        {startOfVisiblePages + i}
                    </button>
                )}

                <button type="button" onClick={() => this.props.handlePageChange(totalPages)}>&raquo;</button>
            </div>
        ) : '';
    }
}

export default Pagination;
