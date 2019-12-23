import React from 'react'
import { connect } from 'react-redux'
import Grid from '@material-ui/core/Grid'

import { actions as seeksActions } from '../../../redux/actions/seeks'
import { seeksSelectors } from '../../../redux/selectors/seeks'
import SeekCard from './SeekCard'


export const SeeksContainer = ({
  seeksAll,
  seeksIsLoading,
  seeksIsError,
  loadSeeksAll
}) => {

  React.useEffect(() => {
    loadSeeksAll()
  }, [loadSeeksAll])



  return (
    <React.Fragment>
      {
        (!seeksIsLoading && seeksAll) &&
        <React.Fragment>
            <p>Number of job seeks in the database: {seeksAll.length}</p>
            <div style={{ padding: '16px' }}>
                <Grid container spacing={2} direction="row" justify="space-evenly" alignItems="center" >
                    {seeksAll.map(seek => {
                        return (
                        <Grid key={seek.id} item xs={12} sm={3}>
                            <SeekCard seek={seek} />
                        </Grid> )
                    })}     
                </Grid>
            </div>
        </React.Fragment>

      }
      {
        (!seeksIsLoading && seeksIsError) &&
        <p>Error at loading job seeks.</p>
      }
      {
        seeksIsLoading &&
        <p>Job seeks are loading ... </p>
      }
    </React.Fragment>

  )
}

const mapStateToProps = (state) => {
  return {
    seeksAll: seeksSelectors.getAll(state),
    seeksIsLoading: seeksSelectors.isFetching(state),
    seeksIsError: seeksSelectors.error(state)
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    loadSeeksAll: () => dispatch(seeksActions.fetchAll())
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SeeksContainer)