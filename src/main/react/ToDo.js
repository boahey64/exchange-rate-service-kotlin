import React from 'react';
import { connect } from 'react-redux';
import { fetchToDos } from "./action"

class ToDo extends React.Component {
    componentDidMount() {
        this.props.dispatch(fetchToDos());
    }

    render() {
        const { error, loading } = this.props;

        if (error) {
            return <div>Error! {error.message}</div>;
        }

        if (loading) {
            return <div>Loading...</div>;
        }

        const last5ToDos = this.props.items.filter(item => item.id < 6);

        return (
            <ul>
                    {last5ToDos.map(item =>
                        <li key={item.id}>{item.id} {item.title} {item.completed}</li>
                    )}
            </ul>
        );
    }
}

const mapStateToProps = state => ({
    items: state.items,
    loading: state.loading,
    error: state.error
});

export default connect(mapStateToProps)(ToDo);