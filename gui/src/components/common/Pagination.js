import React, { Component } from 'react';
import { connect } from 'react-redux';
import { setPagination } from '../../actions';
import './Pagination.scss';

export class Pagination extends Component {

    componentDidMount() {
        const { itemsPerPage } = this.props;
        this.props.setPagination({ itemsPerPage });
    }

    componentDidUpdate(prevProps) {
        const { totalItems, itemsPerPage, pagination } = this.props;
        if (totalItems && pagination && prevProps.totalItems !== totalItems) {
            const totalPages = Math.ceil(totalItems / itemsPerPage);
            if (pagination.currentPage > totalPages) {
                // If several records deleted while on the last page
                // Fall back to newly calculated last page
                this.setPagination(totalPages);
            }
        }
    }

    setPagination(currentPage) {
        const { itemsPerPage } = this.props;
        const pagination = { currentPage, itemsPerPage };
        this.props.setPagination(pagination);
    }

    render() {
        const { totalItems, itemsPerPage, pagination, maxPagesToShow } = this.props;
        const currentPage = pagination && pagination.currentPage ? pagination.currentPage : 1;
        const totalPages = Math.ceil(totalItems / itemsPerPage);
        const visiblePages = totalPages > maxPagesToShow ? maxPagesToShow : totalPages;
        const visiblePageOffset = Math.trunc(visiblePages / 2);
        const startOfVisiblePages = (currentPage + visiblePageOffset) <= totalPages ?
            ((currentPage - visiblePageOffset) > 0 ? (currentPage - visiblePageOffset) : 1) :
            totalPages - visiblePages + 1;

        return totalItems > 0 ? (
            <div className="pagination">
                <button onClick={() => this.setPagination(1)}>&laquo;</button>

                {Array.from(Array(visiblePages), (e, i) =>
                    <button key={startOfVisiblePages + i}
                        disabled={currentPage === (startOfVisiblePages + i)}
                        onClick={() => this.setPagination((startOfVisiblePages + i))}>
                        {startOfVisiblePages + i}
                    </button>
                )}

                <button onClick={() => this.setPagination(totalPages)}>&raquo;</button>
            </div>
        ) : '';
    }
}

const mapStateToProps = (state, props) => {
    return {
        pagination: state.commandReducer.pagination
    }
}

const mapDispatchToProps = dispatch => {
    return {
        setPagination: (pagination) => {
            dispatch(setPagination(pagination));
        }
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Pagination);
