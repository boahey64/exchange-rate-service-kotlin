// @flow

import React from 'react';
import { connect } from 'react-redux';
import {fetchExchangeRate, fetchValidCurrencies} from "./action";
import {fetchExchangeRateHistory} from "../history/action";

class ExchangeRateQueryForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      baseCurrency: props.query.baseCurrency,
      targetCurrency: props.query.targetCurrency,
      date: props.query.date,
      historyQuery: props.historyQuery
    };

    this.handleInputChange = this.handleInputChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  componentDidMount() {
    this.props.dispatch(fetchValidCurrencies());
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
    event.preventDefault();

    this.props.query.baseCurrency = this.state.baseCurrency;
    this.props.query.targetCurrency = this.state.targetCurrency;
    this.props.query.date = this.state.date;

    const convertedDate = this.state.date.replace('-', '/').replace('-', '/');
    this.props.historyQuery.date = 'daily/'+convertedDate;
    console.log("convertedDate historyQuery: " + this.props.historyQuery.date);

    this.props.dispatch(fetchExchangeRate(this.state.date, this.state.baseCurrency, this.state.targetCurrency));
    this.props.dispatch(fetchExchangeRateHistory(this.props.historyQuery.date));
  }

  render() {
    return (
        <div>
          <form onSubmit={this.handleSubmit}>
             <label className="input-label">
              Base Currency:
               <select className="select-box"
                   name="baseCurrency"
                   value={this.state.baseCurrency}
                   onChange={this.handleInputChange}
               >
                 {this.props.validCurrencies.map((e, key) => {
                   return <option key={key} value={e}>{e}</option>;
                 })}
               </select>
            </label>
            <br />
            <label className="input-label">
              Target Currency:
              <select className="select-box"
                  name="targetCurrency"
                  value={this.state.targetCurrency}
                  onChange={this.handleInputChange}
              >
                {this.props.validCurrencies.map((e, key) => {
                  return <option key={key} value={e}>{e}</option>;
                })}
              </select>
            </label>
            <br />
            <label className="input-label">
              Date:
              <input className="input-field"
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
    validCurrencies: state.validCurrencies,
    loading: state.loading,
    error: state.error
});

export default connect(mapStateToProps)(ExchangeRateQueryForm);