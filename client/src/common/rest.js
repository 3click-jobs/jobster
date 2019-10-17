import { env } from "./env";

const rest = {
  comment: {
    // key: 'commentId',
    actions: {
      getAll: {
        url: env.baseUrl + '/jobster/comment/all',
        method: 'GET'
      },
      getAllActive: {
        url: env.baseUrl + '/jobster/comment',
        method: 'GET'
      },
      getAllInactive: {
        url: env.baseUrl + '/jobster/comment/inactive',
        method: 'GET'
      },
      getAllArchived: {
        url: env.baseUrl + '/jobster/comment/archived',
        method: 'GET'
      },
      getById: {
        url: env.baseUrl + '/jobster/comment/{id}',
        method: 'GET'
      },
      getByIdAll: {
        url: env.baseUrl + '/jobster/comment/all/{id}/getByIdFromAll',
        method: 'GET'
      },
      addNewComment: {
        url: env.baseUrl + '/jobster/comment',
        method: 'POST'
      },
    }
  },
  accounts: {
    // key: 'accountId',
    actions: {
      getAll: {
        url: env.baseUrl + '/jobster/accounts',
        method: 'GET'
      },
      getById: {
        url: env.baseUrl + '/jobster/accounts/{id}',
        method: 'GET'
      },
      getAllDeleted: {
        url: env.baseUrl + '/jobster/accounts/deleted',
        method: 'GET'
      },
      getDeletedById: {
        url: env.baseUrl + '/jobster/accounts/deleted/{id}',
        method: 'GET'
      },
      getAllArchived: {
        url: env.baseUrl + '/jobster/accounts/archived',
        method: 'GET'
      },
      getArchivedById: {
        url: env.baseUrl + '/jobster/accounts/archived/{id}',
        method: 'GET'
      },
      addNew: {
        url: env.baseUrl + '/jobster/accounts',
        method: 'POST'
      },
      modify: {
        url: env.baseUrl + '/jobster/accounts/{id}',
        method: 'PUT'
      },
      archive: {
        url: env.baseUrl + '/jobster/accounts/archive/{id}',
        method: 'PUT'
      },
      undelete: {
        url: env.baseUrl + '/jobster/accounts/undelete/{id}',
        method: 'PUT'
      },
      delete: {
        url: env.baseUrl + '/jobster/accounts/{id}',
        method: 'DELETE'
      }
    },
  }
}

export default rest