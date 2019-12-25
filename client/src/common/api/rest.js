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
      get: {
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
      create: {
        url: env.baseUrl + '/jobster/accounts',
        method: 'POST'
      },
      update: {
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
      get: {
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
      create: {
        url: env.baseUrl + '/jobster/users/persons',
        method: 'POST'
      },
      update: {
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
      get: {
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
      create: {
        url: env.baseUrl + '/jobster/users/companies',
        method: 'POST'
      },
      update: {
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
  },
  seeks: {
    actions: {
      get: {
        url: env.baseUrl + '/jobster/seeks',
        method: 'GET'
      },
      getById: {
        url: env.baseUrl + '/jobster/seeks/{id}',
        method: 'GET'
      },
      getAllDeleted: {
        url: env.baseUrl + '/jobster/seeks/deleted',
        method: 'GET'
      },
      getDeletedById: {
        url: env.baseUrl + '/jobster/seeks/deleted/{id}',
        method: 'GET'
      },
      getAllArchived: {
        url: env.baseUrl + '/jobster/seeks/archived',
        method: 'GET'
      },
      getArchivedById: {
        url: env.baseUrl + '/jobster/seeks/archived/{id}',
        method: 'GET'
      },
      create: {
        url: env.baseUrl + '/jobster/seeks',
        method: 'POST'
      },
      decline: {
        url: env.baseUrl + '/jobster/applies/decline',
        method: 'POST'
      },
      accept: {
        url: env.baseUrl + '/jobster/applies/accept',
        method: 'POST'
      },
      update: {
        url: env.baseUrl + '/jobster/seeks/{id}',
        method: 'PUT'
      },
      archive: {
        url: env.baseUrl + '/jobster/seeks/archive/{id}',
        method: 'PUT'
      },
      unDelete: {
        url: env.baseUrl + '/jobster/seeks/undelete/{id}',
        method: 'PUT'
      },
      delete: {
        url: env.baseUrl + '/jobster/seeks/{id}',
        method: 'DELETE'
      }
    }
  },
  jobTypes: {
    actions: {
      get: {
        url: env.baseUrl + '/jobster/jobTypes',
        method: 'GET'
      },
      getById: {
        url: env.baseUrl + '/jobster/jobTypes/{id}',
        method: 'GET'
      },
      getAllDeleted: {
        url: env.baseUrl + '/jobster/jobTypes/deleted',
        method: 'GET'
      },
      getDeletedById: {
        url: env.baseUrl + '/jobster/jobTypes/deleted/{id}',
        method: 'GET'
      },
      getAllArchived: {
        url: env.baseUrl + '/jobster/jobTypes/archived',
        method: 'GET'
      },
      getArchivedById: {
        url: env.baseUrl + '/jobster/jobTypes/archived/{id}',
        method: 'GET'
      },
      create: {
        url: env.baseUrl + '/jobster/jobTypes',
        method: 'POST'
      },
      update: {
        url: env.baseUrl + '/jobster/jobTypes/{id}',
        method: 'PUT'
      },
      archive: {
        url: env.baseUrl + '/jobster/jobTypes/archive/{id}',
        method: 'PUT'
      },
      unDelete: {
        url: env.baseUrl + '/jobster/jobTypes/undelete/{id}',
        method: 'PUT'
      },
      delete: {
        url: env.baseUrl + '/jobster/jobTypes/{id}',
        method: 'DELETE'
      }
    }
  },
  offers: {
    actions: {
      get: {
        url: env.baseUrl + '/jobster/offers',
        method: 'GET'
      },
      getById: {
        url: env.baseUrl + '/jobster/offers/{id}',
        method: 'GET'
      },
      getAllDeleted: {
        url: env.baseUrl + '/jobster/offers/deleted',
        method: 'GET'
      },
      getDeletedById: {
        url: env.baseUrl + '/jobster/offers/deleted/{id}',
        method: 'GET'
      },
      getAllArchived: {
        url: env.baseUrl + '/jobster/offers/archived',
        method: 'GET'
      },
      getArchivedById: {
        url: env.baseUrl + '/jobster/offers/archived/{id}',
        method: 'GET'
      },
      create: {
        url: env.baseUrl + '/jobster/offers',
        method: 'POST'
      },
      decline: {
        url: env.baseUrl + '/jobster/applies/decline',
        method: 'POST'
      },
      accept: {
        url: env.baseUrl + '/jobster/applies/accept',
        method: 'POST'
      },
      update: {
        url: env.baseUrl + '/jobster/offers/{id}',
        method: 'PUT'
      },
      archive: {
        url: env.baseUrl + '/jobster/offers/archive/{id}',
        method: 'PUT'
      },
      unDelete: {
        url: env.baseUrl + '/jobster/offers/undelete/{id}',
        method: 'PUT'
      },
      delete: {
        url: env.baseUrl + '/jobster/offers/{id}',
        method: 'DELETE'
      }
    }
  }
}

export default rest