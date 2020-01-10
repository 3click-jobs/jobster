const companyDto = [
  {
    name: 'companyName',
    type: 'text',
    regexp: '^[_A-Za-z0-9-]{2,}$',
    required: true
  },
  {
    name: 'companyRegistrationNumber',
    type: 'text',
    regexp: '^[0-9]{14,14}$',
    required: true
  },
  {
    name: 'mobilePhone',
    type: 'text',
    regexp: '^([\\(]{0,1}[\\+]{0,1}[\\(]{0,1}([3][8][1]){0,1}[\\)]{0,1}[- \\.\\/]{0,1}[\\(]{0,1}[0]{0,1}[\\)]{0,1}[6]{1,1}([0-6]|[9]){1,1}[\\)]{0,1}[- \\.\\/]{0,1}(([0-9]{6,7})|([0-9]{2,3}[- \\.\\/]{0,1}[0-9]{2,4}[- \\.\\/]{0,1}[0-9]{0,3})))$',
    required: true
  },
  {
    name: 'email',
    type: 'email',
    regexp: '^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$',
    required: true
  },
  {
    name: 'city',
    type: 'text',
    regexp: '^[A-Za-z\\s]+$',
    required: true
  },
  {
    name: 'country',
    type: 'text',
    regexp: '^[A-Za-z\\s]+$',
    required: true
  },
  {
    name: 'iso2Code',
    type: 'text',
    regexp: '^[A-Za-z]{2,3}$',
    required: true
  },
  {
    name: 'countryRegion',
    type: 'text',
    regexp: '^[A-Za-z\\s]{0,}$',
    required: true
  },
  {
    name: 'longitude',
    type: 'number',
    min: 180,
    max: 180
  },
  {
    name: 'latitude',
    type: 'number',
    min: 90,
    max: 90
  },
  {
    name: 'about',
    type: 'text'
  },
  {
    name: 'rating',
    type: 'number',
    min: 0,
    max: 5
  },
  {
    name: 'numberOfRatings',
    type: 'number',
    min: 0
  },
  // {
  //   name: 'jobOffers',
  //   type: 'array'
  // },
  // {
  //   name: 'jobSeeks',
  //   type: 'array'
  // },
  {
    name: 'username',
    type: 'text',
    min: 5,
    max: 20,
    required: true
  },
  {
    name: 'accessRole',
    type: 'select',
    options: [
      'ROLE_ADMIN', 'ROLE_USER', 'ROLE_GUEST'
    ]
  },
  {
    name: 'password',
    type: 'password',
    regexp: '^[A-Za-z0-9]*$',
    min: 5,
    required: true
  },
  {
    name: 'confirmedPassword',
    type: 'password',
    regexp: '^[A-Za-z0-9]*$',
    min: 5,
    required: true
  }
]

export default companyDto