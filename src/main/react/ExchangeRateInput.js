// @flow

import React from 'react';
import { connect } from 'react-redux';
import { fetchExchangeRate } from "./action"

class ExchangeRateInput extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      baseCurrency: "EUR",
      targetCurrency: "USD",
      date: "2019-12-24"
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
//    alert('Base Currency is: ' + this.state.baseCurrency);
//    console.log('baseCurrency: {}', this.state.baseCurrency);
    console.log(' baseCurrency: ' + this.state.baseCurrency);
    console.log(' targetCurrency: ' + this.state.targetCurrency);
    console.log(' date: ' + this.state.date);
    event.preventDefault();
  }

  render() {
    return (
        <div>
          <form onSubmit={this.handleSubmit}>
            <label>
              Base Currency:
              <input
                name="baseCurrency"
                value={this.state.baseCurrency}
                onChange={this.handleInputChange} />
            </label>
            <br />
            <label>
              Target Currency:
              <input
                name="targetCurrency"
                value={this.state.targetCurrency}
                onChange={this.handleInputChange} />
            </label>
            <br />
            <label>
              Date:
              <input
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
    loading: state.loading,
    error: state.error
});

export default connect(mapStateToProps)(ExchangeRateInput);