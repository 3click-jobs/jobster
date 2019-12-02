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
  },
  users: {
    actions: {
      getAll: {
        url: env.baseUrl + '/jobster/users',
        method: 'GET'
      }
    }
  },
  persons: {
    actions: {
      getAll: {
        url: env.baseUrl + '/jobster/users/persons',
        method: 'GET'
      },
      getById: {
        url: env.baseUrl + '/jobster/users/persons/{id}',
        method: 'GET'
      },
      getAllDeleted: {
        url: env.baseUrl + '/jobster/users/persons/deleted',
        method: 'GET'
      },
      getDeletedById: {
        url: env.baseUrl + '/jobster/users/persons/deleted/{id}',
        method: 'GET'
      },
      getAllArchived: {
        url: env.baseUrl + '/jobster/users/persons/archived',
        method: 'GET'
      },
      getArchivedById: {
        url: env.baseUrl + '/jobster/users/persons/archived/{id}',
        method: 'GET'
      },
      addNewPerson: {
        url: env.baseUrl + '/jobster/users/persons',
        method: 'POST'
      },
      modifyPerson: {
        url: env.baseUrl + '/jobster/users/persons/{id}',
        method: 'PUT'
      },
      archive: {
        url: env.baseUrl + '/jobster/users/persons/archive/{id}',
        method: 'PUT'
      },
      unDelete: {
        url: env.baseUrl + '/jobster/users/persons/undelete/{id}',
        method: 'PUT'
      },
      delete: {
        url: env.baseUrl + '/jobster/users/persons/{id}',
        method: 'DELETE'
      }
    },
  },
  home: {
    actions: {
      greeting: {
        url: env.baseUrl,
        method: 'GET'
      }
    }
  },
  companies: {
    actions: {
      getAll: {
        url: env.baseUrl + '/jobster/users/companies',
        method: 'GET'
      },
      getById: {
        url: env.baseUrl + '/jobster/users/companies/{id}',
        method: 'GET'
      },
      getAllDeleted: {
        url: env.baseUrl + '/jobster/users/companies/deleted',
        method: 'GET'
      },
      getDeletedById: {
        url: env.baseUrl + '/jobster/users/companies/deleted/{id}',
        method: 'GET'
      },
      getAllArchived: {
        url: env.baseUrl + '/jobster/users/companies/archived',
        method: 'GET'
      },
      getArchivedById: {
        url: env.baseUrl + '/jobster/users/companies/archived/{id}',
        method: 'GET'
      },
      addNewCompany: {
        url: env.baseUrl + '/jobster/users/companies',
        method: 'POST'
      },
      modifyCompany: {
        url: env.baseUrl + '/jobster/users/companies/{id}',
        method: 'PUT'
      },
      archive: {
        url: env.baseUrl + '/jobster/users/companies/archive/{id}',
        method: 'PUT'
      },
      undelete: {
        url: env.baseUrl + '/jobster/users/companies/undelete/{id}',
        method: 'PUT'
      },
      delete: {
        url: env.baseUrl + '/jobster/users/companies/{id}',
        method: 'DELETE'
      }      
    }
  }
}

export default rest