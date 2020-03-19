// @flow

import React from 'react';
import { connect } from 'react-redux';
import { fetchExchangeRateHistory } from "./action";

class HistoryQueryForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      date: props.historyQuery.date,
    };

    console.log('HistoryQueryForm: ' + this.state.date);

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
    // console.log(' baseCurrency: ' + this.state.baseCurrency);
    //console.log(' targetCurrency: ' + this.state.targetCurrency);
    console.log(' historyQuery.date: ' + this.state.date);
    event.preventDefault();

    // this.props.query.baseCurrency = this.state.baseCurrency;
    // this.props.query.targetCurrency = this.state.targetCurrency;
    this.props.historyQuery.date = this.state.date;

    this.props.dispatch(fetchExchangeRateHistory(this.state.date));
  }

  render() {
    return (
        <div>
          <form onSubmit={this.handleSubmit}>
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
    historyQuery: state.historyQuery,
    loading: state.loading,
    error: state.error
});

export default connect(mapStateToProps)(HistoryQueryForm);