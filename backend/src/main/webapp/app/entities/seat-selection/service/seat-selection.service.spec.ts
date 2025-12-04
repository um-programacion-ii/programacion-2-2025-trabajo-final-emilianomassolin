import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISeatSelection } from '../seat-selection.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../seat-selection.test-samples';

import { RestSeatSelection, SeatSelectionService } from './seat-selection.service';

const requireRestSample: RestSeatSelection = {
  ...sampleWithRequiredData,
  fechaSeleccion: sampleWithRequiredData.fechaSeleccion?.toJSON(),
  expiracion: sampleWithRequiredData.expiracion?.toJSON(),
};

describe('SeatSelection Service', () => {
  let service: SeatSelectionService;
  let httpMock: HttpTestingController;
  let expectedResult: ISeatSelection | ISeatSelection[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SeatSelectionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a SeatSelection', () => {
      const seatSelection = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(seatSelection).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SeatSelection', () => {
      const seatSelection = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(seatSelection).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SeatSelection', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SeatSelection', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SeatSelection', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSeatSelectionToCollectionIfMissing', () => {
      it('should add a SeatSelection to an empty array', () => {
        const seatSelection: ISeatSelection = sampleWithRequiredData;
        expectedResult = service.addSeatSelectionToCollectionIfMissing([], seatSelection);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(seatSelection);
      });

      it('should not add a SeatSelection to an array that contains it', () => {
        const seatSelection: ISeatSelection = sampleWithRequiredData;
        const seatSelectionCollection: ISeatSelection[] = [
          {
            ...seatSelection,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSeatSelectionToCollectionIfMissing(seatSelectionCollection, seatSelection);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SeatSelection to an array that doesn't contain it", () => {
        const seatSelection: ISeatSelection = sampleWithRequiredData;
        const seatSelectionCollection: ISeatSelection[] = [sampleWithPartialData];
        expectedResult = service.addSeatSelectionToCollectionIfMissing(seatSelectionCollection, seatSelection);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(seatSelection);
      });

      it('should add only unique SeatSelection to an array', () => {
        const seatSelectionArray: ISeatSelection[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const seatSelectionCollection: ISeatSelection[] = [sampleWithRequiredData];
        expectedResult = service.addSeatSelectionToCollectionIfMissing(seatSelectionCollection, ...seatSelectionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const seatSelection: ISeatSelection = sampleWithRequiredData;
        const seatSelection2: ISeatSelection = sampleWithPartialData;
        expectedResult = service.addSeatSelectionToCollectionIfMissing([], seatSelection, seatSelection2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(seatSelection);
        expect(expectedResult).toContain(seatSelection2);
      });

      it('should accept null and undefined values', () => {
        const seatSelection: ISeatSelection = sampleWithRequiredData;
        expectedResult = service.addSeatSelectionToCollectionIfMissing([], null, seatSelection, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(seatSelection);
      });

      it('should return initial array if no SeatSelection is added', () => {
        const seatSelectionCollection: ISeatSelection[] = [sampleWithRequiredData];
        expectedResult = service.addSeatSelectionToCollectionIfMissing(seatSelectionCollection, undefined, null);
        expect(expectedResult).toEqual(seatSelectionCollection);
      });
    });

    describe('compareSeatSelection', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSeatSelection(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 6526 };
        const entity2 = null;

        const compareResult1 = service.compareSeatSelection(entity1, entity2);
        const compareResult2 = service.compareSeatSelection(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 6526 };
        const entity2 = { id: 8295 };

        const compareResult1 = service.compareSeatSelection(entity1, entity2);
        const compareResult2 = service.compareSeatSelection(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 6526 };
        const entity2 = { id: 6526 };

        const compareResult1 = service.compareSeatSelection(entity1, entity2);
        const compareResult2 = service.compareSeatSelection(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
