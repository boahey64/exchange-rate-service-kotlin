import React from 'react';
import { connect } from 'react-redux';
import { fetchExchangeRate } from "./action"

class ExchangeRateResult extends React.Component {
    componentDidMount() {
        this.props.dispatch(fetchExchangeRate());
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

        return (
            <ul>
                <li>Current Rate: {exchangeRate.currentRate}</li>
                <li>Average Rate: {exchangeRate.averageRate}</li>
                <li>Trend: {exchangeRate.trend}</li>
            </ul>
        );
    }
}

const mapStateToProps = state => ({
    item: state.item,
    loading: state.loading,
    error: state.error
});

export default connect(mapStateToProps)(ExchangeRateResult);