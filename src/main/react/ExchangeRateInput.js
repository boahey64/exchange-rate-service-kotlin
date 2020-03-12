// @flow

import React from 'react';
import { connect } from 'react-redux';
import { fetchExchangeRate } from "./action"
import styled from "styled-components";
import colors from "./util/colors";

type ItemProps = {
    onCalculateExchangeRate: () => void
};

const StyledInput = styled.input`
    appearance: none;
    box-sizing: border-box;
    border: 1px solid ${colors.grey_300};
    border-radius: 0;
    font-size: 16px;
    height: 40px;
    margin-bottom: 0;
    padding: 10px;
    width: 30%;

    &:focus {
        outline: none;
        padding: 10px 65px 10px 10px;
    }

    @media screen and (min-width: 960px) {
        font-size: 14px;
    }

    // avoid browsers native clear X
    &::-ms-clear,
    &::-ms-reveal {
        display: none;
        height: 0;
        width: 0;
    }
    &::-webkit-search-decoration,
    &::-webkit-search-cancel-button,
    &::-webkit-search-results-button,
    &::-webkit-search-results-decoration {
        display: none;
    }
`;


class ExchangeRateInput extends React.Component {
    componentDidMount() {
        this.props.dispatch(fetchExchangeRate());
    }

    handleButtonClick(param) {
        console.log("handleButtonClick: " + param);
    }

    render() {
        const { error, loading } = this.props;
        console.log("props: " + this.props.item);
        if (error) {
            return <div>Error! {error.message}</div>;
        }

        if (loading) {
            return <div>Loading...</div>;
        }

        const exchangeRate = this.props.item;
        console.log("exchangeRate: " + exchangeRate);
//                        <StyledInput
//                            autoComplete="off"
//                            spellCheck={false}
//                            type="search"
//                            value={props.currentFilterTerm}
//                            onChange={props.onSearch}
//                            smallScreen={props.smallScreen}
//                            data-e2e-search
//                        />
//
        return (
                    <div className="input-field">
                        <StyledInput
                            autoComplete="off"
                        />
                        <StyledInput
                            autoComplete="off"
                        />
                        <StyledInput
                            autoComplete="off"
                        />
                        <div>
                            <button onClick={() => this.handleButtonClick("column")}>submit</button>
                        </div>
                    </div>
        );
    }
}

const mapStateToProps = state => ({
    item: state.item,
    loading: state.loading,
    error: state.error
});

export default connect(mapStateToProps)(ExchangeRateInput);