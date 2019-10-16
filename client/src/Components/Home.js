import React, { Component } from 'react';
import '../App.css';
import { Carousel } from "react-bootstrap";

class Home extends Component {
  constructor(props){
    super(props);
    this.state = {
      index: 0,
      direction: null,
    }
  }

  handleSelect(selectedIndex, e) {
    this.setState({
      index: selectedIndex,
      direction: e.direction
    });
  }


  render() {
    return (

<>
<Carousel  className="intro carousel slide bg-overlay-light h-auto" id="carouselExampleCaptions">
  <Carousel.Item className="carousel-item">
    <img
      className="d-block w-100 img-fluid"
      src="https://images.pexels.com/photos/838413/pexels-photo-838413.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"
      alt="First slide"
    />
    <Carousel.Caption>
      <h3 className="display-4 text-white mb-2 mt-4">First slide label</h3>
      <p className="text-white mb-3 px-5 lead">Nulla vitae elit libero, a pharetra augue mollis interdum.</p>
      <a href="/" className="btn btn-primary btn-capsul px-4 py-2">Search Available Jobs</a>
    </Carousel.Caption>
  </Carousel.Item>
  <Carousel.Item>
    <img
      className="d-block w-100"
      src="https://images.pexels.com/photos/838413/pexels-photo-838413.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"
      alt="Second slide"
    />

    <Carousel.Caption>
      <h3>Second slide label</h3>
      <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
      <a href="/" className="btn btn-primary btn-capsul px-4 py-2">Search Available Jobs</a>
    </Carousel.Caption>
  </Carousel.Item>
  
</Carousel>


<div className="jumbotron">
  <p> text</p>
  </div>
    
{/* 
<section className="intro carousel slide bg-overlay-light h-auto" id="carouselExampleCaptions">
    <ol className="carousel-indicators">
      <li data-target="#carouselExampleCaptions" data-slide-to="0" className="active"></li>
      <li data-target="#carouselExampleCaptions" data-slide-to="1" className=""></li>
    </ol>
    <div className="carousel-inner" role="listbox">
      <div className="carousel-item active">
        <img className="d-block img-fluid" alt="First slide" src="https://images.pexels.com/photos/838413/pexels-photo-838413.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"/>
        <div className="carousel-caption ">
          	<h2 className="display-4 text-white mb-2 mt-4">Lorem ipsum dolor sit amet, consectetur adipiscing elit</h2>
			<p className="text-white mb-3 px-5 lead">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do enim ad minim veniam, </p>
			<a href="/" className="btn btn-primary btn-capsul px-4 py-2">Search Available Jobs</a>
        </div>
      </div>
      <div className="carousel-item">
        <img className="d-block img-fluid" alt="First slide" src="https://www.talkgeo.com/wp-content/uploads/Business-Travel-1.jpg"/>
        <div className="carousel-caption ">
          	<h2 className="display-4 text-white mb-2 mt-4">Lorem ipsum dolor sit amet, consectetur adipiscing elit</h2>
			<p className="text-white mb-3 px-5 lead">Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do enim ad minim veniam, </p>
			<a href="/" className="btn btn-primary btn-capsul px-4 py-2">Explore More</a>
        </div>
      </div>
    </div>
    <a className="carousel-control-prev" href="/" role="button" data-slide="prev">
      <span className="carousel-control-prev-icon" aria-hidden="true"></span>
      <span className="sr-only">Previous</span>
    </a>
    <a className="carousel-control-next" href="/" role="button" data-slide="next">
      <span className="carousel-control-next-icon" aria-hidden="true"></span>
      <span className="sr-only">Next</span>
    </a>
</section>

<div className="jumbotron">
  <p> text</p>
  </div> */}
  </>
    


    );
  }
}
export default Home;