import React, { Component } from 'react';
import Job from './Job';
import Filter from './Filter';
import { JobConsumer } from '../context';

class JobList extends Component {
    render() {        
        return (    
            <React.Fragment>
                <div className="jumbotron job">
                    <div className="row ng-scope">
                        <div className="col-md-3 col-md-push-9">
                            {/* Begin Filter Component */}
                            <Filter/>
                        </div>
                        <div className="col-md-9 col-md-pull-3"> 
                            {/* Begin Job Component */}                                    
                            <JobConsumer>    
                                {value => {                                                 
                                    return value.jobs.map(job => {
                                        return <Job key={job.id} job={job}/>;
                                    });
                                }}
                            </JobConsumer>                                       
                        </div>
                    </div>
                </div>
            </React.Fragment>                 
        );
    }
}
export default JobList;