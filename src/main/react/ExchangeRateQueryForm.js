// @flow

import React from 'react';
import { connect } from 'react-redux';
import { fetchExchangeRate } from "./action"

class ExchangeRateQueryForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      baseCurrency: props.query.baseCurrency,
      targetCurrency: props.query.targetCurrency,
      date: props.query.date,
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
    props.query.baseCurrency = this.state.baseCurrency;
    props.query.targetCurrency = this.state.targetCurrency;
    props.query.date = this.state.date;

    console.log('name: ' + name  + ' value: ' + value);
  }

  handleSubmit(event) {
    console.log('handleSubmit:');
    console.log(' baseCurrency: ' + this.state.baseCurrency);
    console.log(' targetCurrency: ' + this.state.targetCurrency);
    console.log(' date: ' + this.state.date);
    event.preventDefault();

    this.props.dispatch(fetchExchangeRate(this.state.date, this.state.baseCurrency, this.state.targetCurrency));
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
    loading: state.loading,
    error: state.error
});

export default connect(mapStateToProps)(ExchangeRateQueryForm);