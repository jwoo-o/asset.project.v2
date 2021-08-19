export const rank_cd =
    [
      { detailCode: 'R0001', detailName: '사원' },
      { detailCode: 'R0002', detailName: '주임' },
      { detailCode: 'R0003', detailName: '대리' },
      { detailCode: 'R0004', detailName: '과장' },
      { detailCode: 'R0005', detailName: '차장' },
      { detailCode: 'R0006', detailName: '부장' },
      { detailCode: 'R0007', detailName: '이사' },
      { detailCode: 'R0008', detailName: '상무이사' },
      { detailCode: 'R0009', detailName: '전무이사' },
      { detailCode: 'R0010', detailName: '대표이사' }
    ]
export const job_cd =
    [
      { detailCode: 'P0001', detailName: '팀원' },
      { detailCode: 'P0002', detailName: '파트장' },
      { detailCode: 'P0003', detailName: '팀장' },
      { detailCode: 'P0004', detailName: '부서장' },
      { detailCode: 'P0005', detailName: '대표이사' }
    ]

export const level = [
  { detailCode: '1', detailName: '1급' },
  { detailCode: '2', detailName: '2급' },
  { detailCode: '3', detailName: '3급' },
  { detailCode: '4', detailName: '4급' },
  { detailCode: '5', detailName: '5급' }
]

export const defaultSearchForm = {
  orgCode: '',
  orgName: '',
  searchType: '',
  searchWord: '',
  page: 1,
  limit: 10,
  order: 1,
  offset: 1,
  userId: '',
  userName: '',
  approveState: '',
  sort: '',
  seq: ''
}

export const defaultAssetSearchForm = {
  orgCode: '',
  orgName: '',
  page: 1,
  limit: 10,
  order: 1,
  offset: 1,
  userName: '',
  category: '',
  status: '',
  note: '',
  rankCd: '',
  assetId: '',
  startDate: '',
  endDate: ''
}

export const maxFileUploadSize = 100000000
export const maxFileUploadMessage = '100MB 이하의 파일 업로드 가능'
