// @flow

import React from 'react';
import { connect } from 'react-redux';
import { fetchExchangeRate } from "./action";
import {fetchExchangeRateHistory} from "../history/action";

class ExchangeRateQueryForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      baseCurrency: props.query.baseCurrency,
      targetCurrency: props.query.targetCurrency,
      date: props.query.date,
      historyQuery: props.historyQuery,
    };

    this.handleInputChange = this.handleInputChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleInputChange(event) {
    const target = event.target;
    const value = target.type === 'checkbox' ? target.checked : target.value;
    const name = target.name;
    console.log('name: {}', name);

    this.setState({
      [name]: value
    });

    console.log('name: ' + name  + ' value: ' + value);
  }

  handleSubmit(event) {
    console.log('handleSubmit:');
    console.log(' baseCurrency: ' + this.state.baseCurrency);
    console.log(' targetCurrency: ' + this.state.targetCurrency);
    console.log(' date: ' + this.state.date);
    event.preventDefault();

    this.props.query.baseCurrency = this.state.baseCurrency;
    this.props.query.targetCurrency = this.state.targetCurrency;
    this.props.query.date = this.state.date;

    const convertedDate = this.state.date.replace('-', '/').replace('-', '/');
    console.log("convertedDate: " + convertedDate);
    this.props.historyQuery.date = 'daily/'+convertedDate;
    console.log("convertedDate historyQuery: " + this.props.historyQuery.date);

    console.log('before fetchExchangeRate');
    this.props.dispatch(fetchExchangeRate(this.state.date, this.state.baseCurrency, this.state.targetCurrency));
    console.log('fetchExchangeRate');
    this.props.dispatch(fetchExchangeRateHistory(this.props.historyQuery.date));
    console.log('fetchExchangeRateHistory');
  }

  render() {
    return (
        <div>
          <form onSubmit={this.handleSubmit}>
            <label class="input-label">
              Base Currency:
              <input class="input-field"
                name="baseCurrency"
                value={this.state.baseCurrency}
                onChange={this.handleInputChange} />
            </label>
            <br />
            <label class="input-label">
              Target Currency:
              <input class="input-field"
                name="targetCurrency"
                value={this.state.targetCurrency}
                onChange={this.handleInputChange} />
            </label>
            <br />
            <label class="input-label">
              Date:
              <input class="input-field"
                name="date"
                value={this.state.date}
                onChange={this.handleInputChange} />
            </label>
            <br />
            <input type="submit" value="Absenden" />
          </form>
        </div>
    );
  }
}

const mapStateToProps = state => ({
    item: state.item,
    query: state.query,
    historyQuery: state.historyQuery,
    loading: state.loading,
    error: state.error
});

export default connect(mapStateToProps)(ExchangeRateQueryForm);