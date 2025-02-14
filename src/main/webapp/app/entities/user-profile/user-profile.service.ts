import axios from 'axios';

import { type IUserProfile } from '@/shared/model/user-profile.model';

const baseApiUrl = 'api/user-profiles';
const baseSearchApiUrl = 'api/user-profiles/_search?query=';

export default class UserProfileService {
  public search(query): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(`${baseSearchApiUrl}${query}`)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public find(id: number): Promise<IUserProfile> {
    return new Promise<IUserProfile>((resolve, reject) => {
      axios
        .get(`${baseApiUrl}/${id}`)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public retrieve(): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(baseApiUrl)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public delete(id: number): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .delete(`${baseApiUrl}/${id}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public create(entity: IUserProfile): Promise<IUserProfile> {
    return new Promise<IUserProfile>((resolve, reject) => {
      axios
        .post(`${baseApiUrl}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public update(entity: IUserProfile): Promise<IUserProfile> {
    return new Promise<IUserProfile>((resolve, reject) => {
      axios
        .put(`${baseApiUrl}/${entity.id}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public partialUpdate(entity: IUserProfile): Promise<IUserProfile> {
    return new Promise<IUserProfile>((resolve, reject) => {
      axios
        .patch(`${baseApiUrl}/${entity.id}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
