import React from 'react';
import { connect } from 'react-redux';
import { fetchExchangeRateHistory } from "./action";

class HistoryResult extends React.Component {
    render() {
        const { error, loading } = this.props;
        console.log("history props: " + this.props.items);
        if (error) {
            return <div>Error! {error.message}</div>;
        }

        if (loading) {
            return <div>Loading...</div>;
        }

        const historyItems = this.props.items;
        console.log("historyItems: " + historyItems);

        return (
            <div>
                <ul>
                    {historyItems.map(item =>
                        <li key={item.id}>{item.id} {item.dateString} {item.baseCurrency} {item.targetCurrency} {item.currentRate} {item.averageRate} {item.trend}  </li>
                    )}
                </ul>
            </div>
        );
    }
}

const mapStateToProps = state => ({
    items: state.items,
    historyQuery: state.historyQuery,
    loading: state.loading,
    error: state.error
});

export default connect(mapStateToProps)(HistoryResult);