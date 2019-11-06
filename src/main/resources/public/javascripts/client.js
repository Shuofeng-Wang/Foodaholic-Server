class Application extends React.Component {
    render() {
        return (
            <div>
                {/*<PlusButton className="plus-at-the-top"/>*/}
                <Header/>
                {/*<ItemList/>*/}
                {/*<PlusButton className="plus-at-the-bottom"/>*/}
            </div>
        );
    }
}

const Header = () => (
    <header>
        <h1>Foodaholic Admin</h1>
        <p><small>Hi, this is the admin of the foodaholic server. In developing.</small></p>
    </header>
);

ReactDOM.render(<Application/>, document.querySelector("#application"));
