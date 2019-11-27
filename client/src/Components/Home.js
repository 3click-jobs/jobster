import React, { Component } from 'react';
import '../App.css';
import { Carousel } from "react-bootstrap";
import { Link } from 'react-router-dom';

class Home extends Component {
 
  render() {
    return (
    <>
      <Carousel  className="intro carousel slide bg-overlay-light h-auto" id="carouselExampleCaptions">
        <Carousel.Item className="carousel-item">
            <img className="d-block w-100 img-fluid" src="img/carousel-1.jpg" alt="First slide" />
            <Carousel.Caption>
              <h3 className="display-4 text-white mb-2 mt-4">First slide label</h3>
              <p className="text-white mb-3 px-5 lead">Nulla vitae elit libero, a pharetra augue mollis interdum.</p>
              <Link className="btn btn-primary btn-capsul px-4 py-2" to={'/joblist'} id="joblist">Search Available Jobs</Link>
            </Carousel.Caption>
        </Carousel.Item>
        <Carousel.Item>
            <img className="d-block w-100" src="img/carousel-2.jpg" alt="Second slide"/>
            <Carousel.Caption>
              <h3>Second slide label</h3>
              <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
              <Link className="btn btn-primary btn-capsul px-4 py-2" to={'/joblist'} id="joblist">Search Available Jobs</Link>		    
            </Carousel.Caption>
        </Carousel.Item>  
      </Carousel>
      <div className="jumbotron">
        <p> text</p>
      </div>   
    </>
    );
  }
}
export default Home;