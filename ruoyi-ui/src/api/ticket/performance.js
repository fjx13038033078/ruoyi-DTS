import request from '@/utils/request'

// 查询演出列表
export function listPerformance(query) {
  return request({
    url: '/ticket/performance/list',
    method: 'get',
    params: query
  })
}

// 查询演出详细
export function getPerformance(performanceId) {
  return request({
    url: '/ticket/performance/' + performanceId,
    method: 'get'
  })
}

// 新增演出
export function addPerformance(data) {
  return request({
    url: '/ticket/performance',
    method: 'post',
    data: data
  })
}

// 修改演出
export function updatePerformance(data) {
  return request({
    url: '/ticket/performance/edit',
    method: 'post',
    data: data
  })
}

// 删除演出
export function delPerformance(performanceIds) {
  return request({
    url: '/ticket/performance/remove',
    method: 'post',
    params: { performanceIds }
  })
}
