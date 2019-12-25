// import { createSelector } from 'reselect'
import objectPath from 'object-path'

// console.log('OBJECT PATH: ', objectPath)

const getLocalSelectors = (idKey = 'id') => ({
  getAll: (state) => { 
    // console.log('INSIDE LOCAL SELECTOR allids: ', state.allIds)
    // console.log('INSIDE LOCAL SELECTOR: ', state.allIds.map(id => state.byId[id]))
    return state.allIds.map(id => state.byId[id]) 
  },
  byId: function(state, id) {
    // console.log('ALL: ', this.getAll(state))
    // maybe we got here from router props
    let realId = id
    // console.log('idKey (entity): ', idKey, ' HELPERS.js (selectorCreator) STATE: ', state)
    // console.log('ID: ', id, ' ID match: ', id.match)
    if (id && id.match && id.match.params) {
      realId = parseInt(id.match.params.id)
    }
    // IMPORTANT!!! if you want to use 'this', just make it a function instead of an arrow function

    // const all = this.getAll(state)
    // console.log('IDKEY: ', idKey, ' all[1]: ', all[1])
    // return all.filter(e => e[idKey] === id)
    // much better would be just to use
    return state.byId[realId]
  },
  byQuery: (state, query) => {
    if (!state.queries[query]) return []
    return state.queries[query].map(id => state.byId[id])
  },
  error: (state) => state.errorMessage,
  isFetching: (state) => state.isFetching
})

// 
// Example
// const teacherSelectors = getLocalSelectors('teacherId')
//

const getGlobalSelectors = (selectors, path) => ({
  getAll: (state) => selectors.getAll(objectPath.get(state, path)),
  byId: (state, id) => selectors.byId(objectPath.get(state, path), id),
  byQuery: (state, query) => selectors.byQuery(objectPath.get(state, path), query),
  error: (state) => selectors.error(objectPath.get(state, path)),
  isFetching: (state) => selectors.isFetching(objectPath.get(state, path))
})

//
//  Example
//  import { teacherSelectors as localTeacherSelectors } from '.path.'
//
//  const teacherSelectors = getGlobalSelectors(localTeacherSelectors, 'entities.teachers')
//

export { getLocalSelectors, getGlobalSelectors }