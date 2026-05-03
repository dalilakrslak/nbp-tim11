export type Sort = {
  sorted: boolean;
  unsorted: boolean;
  empty: boolean;
};

export type Page = {
  size: number;
  number: number;
  totalElements: number;
  totalPages: number;
};

export type ResponseType<T> = {
  content: T[];
  page: Page;
};
