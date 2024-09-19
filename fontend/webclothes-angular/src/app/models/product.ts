import { ProductImage } from './product.image';

export interface Product {
  id: number;
  name: string;
  price: number;
  thumbnail: string;
  quantity: number;
  description: string;
  categoryId: number;
  url: string;
  product_images: ProductImage[];
}
