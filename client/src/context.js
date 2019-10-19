import React, { Component } from 'react';
import { jobStore, jobDetails } from './data';

const JobContext = React.createContext();

class JobProvider extends Component {
  state = {
    jobs: [],
    jobDetails: jobDetails
  };

  // Make copy of jobStore data
  componentDidMount() {
    this.setJobs();
  }
  setJobs = () => {
    let tempJobs = [];
    jobStore.forEach(job => {
      const singleJob = {...job};
      tempJobs = [...tempJobs, singleJob];
    })
    this.setState( () => {
      return {jobs: tempJobs};
    })
  };


  handleDetail = () => {
    console.log("hello from detail")
  };

     
  render() {
    return (       
       <JobContext.Provider 
        value={{
          ...this.state,
          handleDetail: this.handleDetail
        }}       
        >
           {this.props.children}
       </JobContext.Provider>   
    )
  }
}

const JobConsumer = JobContext.Consumer;

export { JobProvider, JobConsumer };