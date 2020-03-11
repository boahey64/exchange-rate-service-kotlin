import React from 'react';
import { connect } from 'react-redux';
import { fetchExchangeRate } from "./action"

class ExchangeRateQuery extends React.Component {
    componentDidMount() {
        this.props.dispatch(fetchExchangeRate());
    }

    render() {
        const { error, loading } = this.props;
        console.log("props: " + this.props.currentRate);
        if (error) {
            return <div>Error! {error.message}</div>;
        }

        if (loading) {
            return <div>Loading...</div>;
        }

        const exchangeRate = this.props.item;
        console.log("exchangeRate: " + exchangeRate);

        const temp = "my temp";
        return (
            <div>
                {temp} ABC {exchangeRate}
            </div>
        );
    }
}

const mapStateToProps = state => ({
    currentRate: state.currentRate,
    loading: state.loading,
    error: state.error
});

export default connect(mapStateToProps)(ExchangeRateQuery);