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
                    <div className="input-label">
                        Base Currency:
                        <div>
                            {query.baseCurrency}
                        </div>
                    </div>
                    <div className="input-label">
                        Target Currency:
                        <div>
                            {query.targetCurrency}
                        </div>
                    </div>
                    <div className="input-label">
                        Date:
                        <div>
                            {query.date}
                        </div>
                    </div>
                </div>
                <br/>
                <div>
                    <div className="result-label">
                        Current Rate:
                        <div className="result">
                            {exchangeRate.currentRate}
                        </div>
                    </div>
                    <div className="result-label">
                        Average Rate:
                        <div className="result">
                            {exchangeRate.averageRate}
                        </div>
                    </div>
                    <div className="result-label">
                        Trend:
                        <div className="result">
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