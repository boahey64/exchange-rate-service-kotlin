import React from 'react';
import { connect } from 'react-redux';

class ExchangeRateResult extends React.Component {

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
        const query = this.props.query;
        console.log("exchangeRate: " + exchangeRate);

        return (
            <div>
                <div>
                    <div class="input-label">
                        Base Currency:
                        <div>
                            {query.baseCurrency}
                        </div>
                    </div>
                    <div class="input-label">
                        Target Currency:
                        <div>
                            {query.targetCurrency}
                        </div>
                    </div>
                    <div class="input-label">
                        Date:
                        <div>
                            {query.date}
                        </div>
                    </div>
                </div>
                <br/>
                <div>
                    <div class="result-label">
                        Current Rate:
                        <div class="result">
                            {exchangeRate.currentRate}
                        </div>
                    </div>
                    <div class="result-label">
                        Average Rate:
                        <div class="result">
                            {exchangeRate.averageRate}
                        </div>
                    </div>
                    <div class="result-label">
                        Trend:
                        <div class="result">
                            {exchangeRate.trend}
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

const mapStateToProps = state => ({
    item: state.item,
    query: state.query,
    loading: state.loading,
    error: state.error
});

export default connect(mapStateToProps)(ExchangeRateResult);